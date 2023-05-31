import React from 'react'
import { useState } from 'react'

const SearchBar = ({ onSearch }) => {
    const [searchTerm, setSearchTerm] = useState('');
  
    const handleChange = (event) => {
      setSearchTerm(event.target.value);
    };
  
    const handleSubmit = (event) => {
      event.preventDefault();
      onSearch(searchTerm);
    };
  
    return (
      <div className='SearchBar'>
        <form onSubmit={handleSubmit}>
          <input
            className='search'
            placeholder='Search...'
            type='search'
            value={searchTerm}
            onChange={handleChange}
          />
          <button className='submit' type='submit'>Search</button>
        </form>
      </div>
    );
  };

export default SearchBar