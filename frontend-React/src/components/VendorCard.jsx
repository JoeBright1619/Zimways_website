import { useNavigate } from 'react-router-dom';
import { FaStar, FaPhone, FaMapMarkerAlt, FaEnvelope } from 'react-icons/fa';

function VendorCard({ vendor }) {
  const navigate = useNavigate();
  const statusColors = {
    OPEN: 'bg-green-500',
    CLOSED: 'bg-red-500',
    BUSY: 'bg-yellow-500'
  };

  return (
    <div 
      onClick={() => navigate(`/vendor/${vendor.id}`)}
      className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-xl transition-all duration-300 cursor-pointer transform hover:-translate-y-1"
    >
      {/* Vendor Image */}
      <div className="h-48 bg-gray-200 relative">
        {vendor.imageUrl ? (
          <img 
            src={vendor.imageUrl} 
            alt={vendor.name}
            className="w-full h-full object-cover"
          />
        ) : (
          <div className="w-full h-full flex items-center justify-center text-gray-400">
            No Image Available
          </div>
        )}
        {/* Status Badge */}
        <div className={`absolute top-2 right-2 px-2 py-1 rounded-full text-white text-sm ${statusColors[vendor.status]}`}>
          {vendor.status}
        </div>
      </div>

      {/* Vendor Info */}
      <div className="p-4">
        <div className="flex justify-between items-start mb-2">
          <h3 className="text-xl font-bold text-gray-800">{vendor.name}</h3>
          <div className="flex items-center">
            <FaStar className="text-yellow-400 mr-1" />
            <span className="font-semibold">{vendor.averageRating.toFixed(1)}</span>
            <span className="text-gray-500 text-sm ml-1">({vendor.totalRatings})</span>
          </div>
        </div>

        <p className="text-gray-600 text-sm mb-3 line-clamp-2">{vendor.description}</p>

        {/* Contact Info */}
        <div className="space-y-2 text-sm text-gray-600">
          <div className="flex items-center">
            <FaMapMarkerAlt className="mr-2 text-primary" />
            <span>{vendor.location}</span>
          </div>
          <div className="flex items-center">
            <FaPhone className="mr-2 text-primary" />
            <span>{vendor.phone}</span>
          </div>
          <div className="flex items-center">
            <FaEnvelope className="mr-2 text-primary" />
            <span className="truncate">{vendor.email}</span>
          </div>
        </div>
      </div>
    </div>
  );
}

export default VendorCard; 