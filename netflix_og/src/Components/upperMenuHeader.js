import {React, useEffect, useState, useRef } from "react";
import { useNavigate } from 'react-router-dom';

function Header({ setResults, setShowSearchModal }) {
	const [searchText, setSearchText] = useState('');
	const [isOpen, setIsOpen] = useState(false);
	const [isAdmin, setIsAdmin] = useState(false);

	const navigate = useNavigate();
	const searchBarRef = useRef(null);

	const checkAdmin = async () => {
		const user = JSON.parse(sessionStorage.getItem("user"));
		try {
			const response = await fetch(`http://localhost:3001/api/users/${user._id}`, {
				method: 'GET'
			});
			if (response.ok) {
				
				const data = await response.json();
				return data.Admin;
			} else {
				alert('Failed to fetch user data');
				return false;
			}
		} catch (error) {
			console.error('Failed to fetch search results:', error);
		}
	}
	const handleSearch = async () => {
		if (searchText.trim() === '') {
			alert('Titles, People, Genres');
			return;
		}

		try {
			const response = await fetch(`http://localhost:3001/api/movies/search/${encodeURIComponent(searchText)}`);
			let data;
			if (!response.ok) {
				data = null;
			} else {
				data = await response.json();
			}
			setResults(data);
			setShowSearchModal(true);
		} catch (error) {
			console.error('Failed to fetch search results:', error);
			setResults([]);
		}
	};
	// Making the search Bar disappear when clicking outside
	const handleOutsideClick = (event) => {
		if (searchBarRef.current && !searchBarRef.current.contains(event.target)) {
			setIsOpen(false);
			setSearchText('');
		}
	};
	useEffect(() => {
		if (isOpen) {
			// Listen to outside click event when search box is open
			document.addEventListener('mousedown', handleOutsideClick);
		}
		return () => {
			// Closing listener when box is closed
			document.removeEventListener('mousedown', handleOutsideClick);
		};
	}, [isOpen]);

	useEffect(() => {
        checkAdmin().then((result) => setIsAdmin(result));
    }, []);

	return (
		<header>
			<a href="/home" className="logo">
				<img src="/images/favicon.ico" alt="Project Logo" className="logo-image" />
			</a>
			<nav>
				<button onClick={() => (window.location.href = '/home')} className='header button'>Home</button>
				<button onClick={() => navigate('/categories')} className='header button'>Categories</button>
				{isAdmin && <button>Manage</button>}
				{!isOpen ? (<button onClick={() => setIsOpen(true)} className='header button'>Search</button>) :
					(<div className="search-bar" ref={searchBarRef}>
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
}

export default Header;