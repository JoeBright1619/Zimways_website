@Component
public class FirebaseTokenFilter extends OncePerRequestFilter {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String idToken = header.substring(7);

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String email = decodedToken.getEmail();
            String name = decodedToken.getName();
            String uid = decodedToken.getUid();

            // Now link Firebase user with your DB user (Customer)
            Customer customer = customerRepository.findByEmail(email)
                .orElseGet(() -> {
                    // Create one if not found
                    Customer newCustomer = new Customer();
                    newCustomer.setEmail(email);
                    newCustomer.setName(name != null ? name : "Firebase User");
                    newCustomer.setPhone("0000000000"); // fallback
                    newCustomer.setPassword(uid); // not used for login, just filled
                    return customerRepository.save(newCustomer);
                });

            // You can set the user in SecurityContext if you're using Spring Security
            request.setAttribute("firebaseUser", customer);

        } catch (FirebaseAuthException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Firebase token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
