/* eslint-disable no-unused-vars */
import { useState, useEffect } from 'react';
import { GiHamburgerMenu } from "react-icons/gi";
import SearchBar from "../components/SearchBar";

function Home({ logout }) {
  const [menuItems, setMenuItems] = useState([]);
  const [cart, setCart] = useState([]);
  const [restaurants, setRestaurants] = useState([]);
  const [showNav, setShowNav] = useState(false);

  useEffect(() => {
    const mockMenu = [
      { id: 1, name: 'Pizza', description: 'Cheesy pizza with tomato sauce', price: 8000, category: 'Meals' },
      { id: 2, name: 'Burger', description: 'Grilled beef burger with fries', price: 5000, category: 'Meals' },
      { id: 3, name: 'Fanta', description: 'Cold orange soda', price: 1000, category: 'Drinks' },
    ];
    setMenuItems(mockMenu);

    const mockRestaurants = [
      { id: 1, name: 'Pizza Palace', description: 'Best pizzas in town' },
      { id: 2, name: 'Burger Haven', description: 'Delicious burgers and fries' },
    ];
    setRestaurants(mockRestaurants);
  }, []);

  const addToCart = (item) => setCart((prev) => [...prev, item]);
  const removeFromCart = (indexToRemove) => setCart((prev) => prev.filter((_, index) => index !== indexToRemove));
  const total = cart.reduce((sum, item) => sum + item.price, 0);

  const categories = [...new Set(menuItems.map((item) => item.category))];

  return (
    <div className="min-h-screen min-w-screen bg-background text-text">
    <header className="bg-primary text-white p-4 flex items-center justify-between">
  <h1 className="text-2xl font-bold flex items-center font-primary text-background">
    ZimWays
  </h1>
  <SearchBar />
  <GiHamburgerMenu className="text-background mr-5 cursor-pointer hover:text-white" size={40} onClick={() => setShowNav(!showNav)} />
</header>
{showNav && (
  <nav className="bg-gray-100 p-4 shadow-md">
    <ul className="space-y-2">
      <li>
        <a href="#restaurants" className="text-blue-500 hover:underline">
          Restaurants
        </a>
      </li>
      <li>
        <a href="#menu" className="text-blue-500 hover:underline">
          Menu
        </a>
      </li>
      <li>
        <a href="#cart" className="text-blue-500 hover:underline">
          Cart
        </a>
      </li>
    </ul>
  </nav>
)}
      <main className="p-4">
        <section id="restaurants" className="mb-8">
          <h2 className="text-xl font-bold mb-4">Restaurants</h2>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            {restaurants.map((restaurant) => (
              <div key={restaurant.id} className="bg-white p-4 rounded shadow">
                <h3 className="text-lg font-bold">{restaurant.name}</h3>
                <p className="text-gray-600">{restaurant.description}</p>
                <button className="mt-2 bg-blue-500 text-white py-1 px-4 rounded hover:bg-blue-600">
                  View Menu
                </button>
              </div>
            ))}
          </div>
        </section>

        <section id="menu" className="mb-8">
          <h2 className="text-xl font-bold mb-4">Menu</h2>
          {categories.map((category) => (
            <div key={category} className="mb-6">
              <h3 className="text-lg font-bold mb-2">{category}</h3>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                {menuItems
                  .filter((item) => item.category === category)
                  .map((item) => (
                    <div key={item.id} className="bg-white p-4 rounded shadow">
                      <h4 className="font-bold">{item.name}</h4>
                      <p className="text-gray-600">{item.description}</p>
                      <p className="font-bold">{item.price.toLocaleString()} RWF</p>
                      <button
                        onClick={() => addToCart(item)}
                        className="mt-2 bg-blue-500 text-white py-1 px-4 rounded hover:bg-blue-600"
                      >
                        Add to Cart
                      </button>
                    </div>
                  ))}
              </div>
            </div>
          ))}
        </section>

        <section id="cart">
          <h2 className="text-xl font-bold mb-4">Your Cart</h2>
          {cart.length === 0 ? (
            <p className="text-gray-600">Your cart is empty</p>
          ) : (
            <div className="bg-white p-4 rounded shadow">
              <ul className="mb-4">
                {cart.map((item, index) => (
                  <li key={index} className="flex justify-between items-center mb-2">
                    <span>{item.name}</span>
                    <span>{item.price.toLocaleString()} RWF</span>
                    <button
                      onClick={() => removeFromCart(index)}
                      className="text-red-500 hover:underline"
                    >
                      Remove
                    </button>
                  </li>
                ))}
              </ul>
              <p className="font-bold">Total: {total.toLocaleString()} RWF</p>
            </div>
          )}
        </section>
      </main>
    </div>
  );
}

export default Home;