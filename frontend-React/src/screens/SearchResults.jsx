import React from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import ItemsList from '../components/ItemsList';
import VendorsList from '../components/VendorsList';

function SearchResults() {
  const location = useLocation();
  const navigate = useNavigate();
  const { results, searchTerm, searchCategory, context } = location.state || {};
  const searchParams = new URLSearchParams(location.search);
  const type = searchParams.get('type') || 'all';

  if (!results) {
    return (
      <div className="min-h-screen bg-background p-4">
        <h1 className="text-2xl font-bold mb-4">No search results found</h1>
        <button
          onClick={() => navigate(-1)}
          className="text-primary hover:text-primary-dark"
        >
          Go back
        </button>
      </div>
    );
  }

  const renderResults = () => {
    if (type === 'all' && context === 'home') {
      return (
        <>
          {results.items?.length > 0 && (
            <section className="mb-8">
              <h2 className="text-xl font-bold mb-4">Products</h2>
              <ItemsList items={results.items} />
            </section>
          )}
          {results.vendors?.length > 0 && (
            <section className="mb-8">
              <h2 className="text-xl font-bold mb-4">Restaurants</h2>
              <VendorsList vendors={results.vendors} />
            </section>
          )}
        </>
      );
    }

    if (type === 'items' || context === 'items') {
      return (
        <section className="mb-8">
          <h2 className="text-xl font-bold mb-4">Products</h2>
          <ItemsList items={Array.isArray(results) ? results : results.items} />
        </section>
      );
    }

    if (type === 'vendors' || context === 'vendors') {
      return (
        <section className="mb-8">
          <h2 className="text-xl font-bold mb-4">Restaurants</h2>
          <VendorsList vendors={Array.isArray(results) ? results : results.vendors} />
        </section>
      );
    }

    return null;
  };

  return (
    <div className="min-h-screen bg-background p-4">
      <div className="max-w-7xl mx-auto">
        <div className="flex items-center justify-between mb-6">
          <h1 className="text-2xl font-bold">
            Search Results for "{searchTerm}"
            {searchCategory !== 'all' && ` in ${searchCategory}`}
          </h1>
          <button
            onClick={() => navigate(-1)}
            className="text-primary hover:text-primary-dark"
          >
            Go back
          </button>
        </div>

        {renderResults()}

        {(!results.items?.length && !results.vendors?.length && 
          !Array.isArray(results)?.length) && (
          <div className="text-center py-8">
            <p className="text-gray-600">No results found</p>
          </div>
        )}
      </div>
    </div>
  );
}

export default SearchResults; 