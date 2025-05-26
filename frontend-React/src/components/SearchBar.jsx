/* eslint-disable no-unused-vars */
import React, { useState, useEffect } from "react";
import { BsSearch } from "react-icons/bs";
import { useNavigate, useLocation } from 'react-router-dom';
import { searchItemsByKeyword } from '../api/itemApi';
import { searchVendorsByName } from '../api/vendorApi';

function SearchBar({ context = 'home' }) {
  const [searchTerm, setSearchTerm] = useState('');
  const [searchCategory, setSearchCategory] = useState('all');
  const navigate = useNavigate();
  const location = useLocation();

  // Define available search categories based on context
  const getSearchCategories = () => {
    switch (context) {
      case 'home':
        return [
          { value: 'all', label: 'All' },
          { value: 'items', label: 'Products' },
          { value: 'vendors', label: 'Restaurants' }
        ];
      case 'items':
        return [
          { value: 'all', label: 'All Products' },
          { value: 'name', label: 'By Name' },
          { value: 'category', label: 'By Category' }
        ];
      case 'vendors':
        return [
          { value: 'all', label: 'All Restaurants' },
          { value: 'name', label: 'By Name' },
          { value: 'location', label: 'By Location' }
        ];
      default:
        return [{ value: 'all', label: 'All' }];
    }
  };

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!searchTerm.trim()) return;

    try {
      let searchResults;
      const searchParams = new URLSearchParams();

      switch (searchCategory) {
        case 'items':
          searchResults = await searchItemsByKeyword(searchTerm);
          searchParams.set('type', 'items');
          break;
        case 'vendors':
          searchResults = await searchVendorsByName(searchTerm);
          searchParams.set('type', 'vendors');
          break;
        case 'all':
          // For home context, search both items and vendors
          if (context === 'home') {
            const [items, vendors] = await Promise.all([
              searchItemsByKeyword(searchTerm),
              searchVendorsByName(searchTerm)
            ]);
            searchResults = { items, vendors };
            searchParams.set('type', 'all');
          } else {
            // For other contexts, search based on the context
            searchResults = context === 'items' 
              ? await searchItemsByKeyword(searchTerm)
              : await searchVendorsByName(searchTerm);
            searchParams.set('type', context);
          }
          break;
        default:
          return;
      }

      searchParams.set('q', searchTerm);
      searchParams.set('category', searchCategory);
      
      // Navigate to search results page with the search parameters
      navigate(`/search?${searchParams.toString()}`, { 
        state: { 
          results: searchResults,
          searchTerm,
          searchCategory,
          context
        }
      });
    } catch (error) {
      console.error('Search failed:', error);
    }
  };

  // Update search category when context changes
  useEffect(() => {
    setSearchCategory('all');
  }, [context]);

  return (
    <form onSubmit={handleSearch} className="flex items-center bg-gray-100 text-black rounded focus-within:ring-2 focus-within:ring-blue-500 w-140 max-w-200 mx-4">
      <select
        value={searchCategory}
        onChange={(e) => setSearchCategory(e.target.value)}
        className="bg-gray-100 text-black px-2 py-2 rounded-l focus:outline-none"
      >
        {getSearchCategories().map(category => (
          <option key={category.value} value={category.value}>
            {category.label}
          </option>
        ))}
      </select>
      <input
        type="text"
        value={searchTerm}
        onChange={(e) => setSearchTerm(e.target.value)}
        placeholder={`Search ${context === 'home' ? 'products or restaurants' : context}...`}
        className="bg-gray-100 text-black px-4 py-2 flex-grow focus:outline-none"
      />
      <button 
        type="submit"
        className="bg-blue-500 text-white px-4 py-2 rounded-r hover:bg-blue-600 transition-colors"
      >
        <BsSearch className="text-lg" />
      </button>
    </form>
  );
}

export default SearchBar;