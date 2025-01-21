// Header.js
import React, { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';

const Header = () => {
    const [isOpen, setIsOpen] = useState(false);
    const [searchText, setSearchText] = useState('');
    const searchBarRef = useRef(null);
    const navigate = useNavigate();

    const handleSearch = () => {
        console.log(`Searching for: ${searchText}`);
        // Add your search logic here
    };

    return (
        <header>
            <a href="/home" className="logo">
                <img src="/images/favicon.ico" alt="Project Logo" className="logo-image" />
            </a>
            <nav>
                <button onClick={() => (window.location.href = '/home')} className='header button'>Home</button>
                <button onClick={() => navigate('/categories')} className='header button'>Categories</button>
                <button>Manage</button>
                {!isOpen ? (
                    <button onClick={() => setIsOpen(true)} className='header button'>Search</button>
                ) : (
                    <div className="search-bar" ref={searchBarRef}>
                        <input
                            type="text"
                            className="search-input"
                            placeholder="Titles, People, Genres"
                            value={searchText}
                            onChange={(e) => setSearchText(e.target.value)}
                            onKeyDown={(e) => {
                                if (e.key === 'Enter') {
                                    handleSearch();
                                }
                            }}
                            autoFocus
                        />
                    </div>
                )}
            </nav>
        </header>
    );
};

export default Header;
