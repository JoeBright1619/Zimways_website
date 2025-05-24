import VendorCard from "./VendorCard";

function VendorsList({ vendors, loading, error }) {
    if (loading) return <div className="text-center">Loading vendors...</div>;
    if (error) return <div className="text-red-500 text-center">{error}</div>;

    return vendors.map((vendor) => (
        <VendorCard key={vendor.id} vendor={vendor} />
    ));
}

export default VendorsList; 