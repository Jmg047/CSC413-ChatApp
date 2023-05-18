import React from 'react'

const SearchBar = () => {

    return (
        <div className='SearchBar'>
            <input className="search" placeholder="" type="search"/>
            <button className="submit" type="submit">Search</button>
        </div>
      
    )
}

export default SearchBar