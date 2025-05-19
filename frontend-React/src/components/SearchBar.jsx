import React from "react";
import { BsSearch } from "react-icons/bs";


function SearchBar() {
  return (
    <div className="flex items-center bg-gray-100 text-black rounded focus-within:ring-2 focus-within:ring-blue-500 w-140 max-w-200 mx-4">
      {/* Dropdown */}
      <select
        className="bg-gray-100 text-black px-2 py-2 rounded-l focus:outline-none"
      >
        <option value="all">All</option>
        <option value="restaurants">Restaurants</option>
        <option value="menu">Menu</option>
        <option value="cart">Cart</option>
      </select>
      {/* Input */}
      <input
        type="text"
        placeholder="Search..."
        className="bg-gray-100 text-black px-4 py-2 flex-grow focus:outline-none"
      />
      {/* Search Button */}
      <button className="bg-blue-500 text-white px-4 py-2 rounded-r hover:bg-blue-600">
        <BsSearch className="text-lg" />
      </button>
    </div>
  );
}

export default SearchBar;