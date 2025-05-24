import { useState } from 'react';
import { useUser } from '../context/UserContext';

function ItemCard({ item, onAddToCart }) {
  const { user } = useUser();
  const [isAdding, setIsAdding] = useState(false);

  const handleAddToCart = async () => {
    if (isAdding) return;
    setIsAdding(true);
    try {
      await onAddToCart(item);
    } finally {
      setIsAdding(false);
    }
  };

  return (
    <div className="flex-none w-64 bg-white rounded-lg shadow-md p-4 m-2 hover:shadow-lg transition-shadow">
      <div className="h-40 bg-gray-200 rounded-md mb-3">
        {/* Placeholder for item image */}
        {item.imageUrl ? (
          <img src={item.imageUrl} alt={item.name} className="w-full h-full object-cover" />
        ): (
        <div className="w-full h-full flex items-center justify-center text-gray-400">
        No Image
      </div>
        )
    }
        
      </div>
      <h3 className="font-bold text-lg mb-1">{item.name}</h3>
      <p className="text-gray-600 text-sm mb-2 line-clamp-2">{item.description}</p>
      <div className="flex justify-between items-center">
        <span className="font-bold text-primary">{item.price.toLocaleString()} RWF</span>
        {user && (
          <button
            onClick={handleAddToCart}
            disabled={isAdding}
            className={`bg-primary text-white px-3 py-1 rounded-md transition-colors ${
              isAdding ? 'opacity-50 cursor-not-allowed' : 'hover:bg-primary-dark'
            }`}
          >
            {isAdding ? 'Adding...' : 'Add to Cart'}
          </button>
        )}
      </div>
    </div>
  );
}

export default ItemCard; 