import ItemCard from './ItemCard';

function ItemsList({ items, loading, error, onAddToCart }) {
  if (loading) return <div className="text-center">Loading items...</div>;
  if (error) return <div className="text-red-500 text-center">{error}</div>;

  return items.map((item) => (
    <ItemCard 
      key={item.id} 
      item={item} 
      onAddToCart={onAddToCart}
    />
  ));
}

export default ItemsList; 