import { React, useEffect, useState, useRef } from "react";
import { useNavigate } from 'react-router-dom';
import './upperMenu.css';

function Header({ setResults, setShowSearchModal }) {
	const [searchText, setSearchText] = useState('');
	const [isOpen, setIsOpen] = useState(false);
	const [isAdmin, setIsAdmin] = useState(false);
	const [mode, setMode] = useState("Dark Mode");
	const switchButtonRef = useRef('');

	const navigate = useNavigate();
	const searchBarRef = useRef(null);

    // To hold username and photo
    const [userName, setUserName] = useState('');
       const [userImage, setUserImage] = useState('');

	useEffect(() => {
    	const user = JSON.parse(sessionStorage.getItem("user"));
    	if (user) {
    		setUserName(user.UserName); // Saving user name
    		setUserImage(user.ProfileImage); // saving path to profile
    	}
    }, []);

	// Change the theme of the page
	useEffect(() => {
		const header = document.querySelector("header");
		header.classList.toggle("headerDark", mode === "Dark Mode");
		header.classList.toggle("headerLight", mode === "Light Mode");
	
		const buttons = document.querySelectorAll(".button");
		buttons.forEach(button => {
			button.classList.toggle("buttonDark", mode === "Dark Mode");
			button.classList.toggle("buttonLight", mode === "Light Mode");
		});
	
		// Update the switch bottom:
		if (switchButtonRef.current) {
			switchButtonRef.current.classList.toggle("buttonDark", mode === "Dark Mode");
			switchButtonRef.current.classList.toggle("buttonLight", mode === "Light Mode");
		}
	}, [mode, isAdmin, isOpen]);
	

	// Toggle the mode and save it to localStorage
	const toggleMode = () => {
		const newMode = mode === "Dark Mode" ? "Light Mode" : "Dark Mode";
		setMode(newMode);
		localStorage.setItem("themeMode", newMode); // Save mode to localStorage
	};

	// Load mode from localStorage on component mount
	useEffect(() => {
		const savedMode = localStorage.getItem("themeMode");
		if (savedMode) {
			setMode(savedMode);
		}
	}, []);

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

	const handleLogout = () => {
		sessionStorage.removeItem('token');
		sessionStorage.removeItem('user');
		navigate('/');
	}

	useEffect(() => {
		if (isOpen) {
			document.addEventListener('mousedown', handleOutsideClick);
		}
		return () => {
			document.removeEventListener('mousedown', handleOutsideClick);
		};
	}, [isOpen]);

	useEffect(() => {
		checkAdmin().then((result) => setIsAdmin(result));
	}, []);

	const handleOutsideClick = (event) => {
		if (searchBarRef.current && !searchBarRef.current.contains(event.target)) {
			setIsOpen(false);
			setSearchText('');
		}
	};

	return (
		<header >
			<a href="/home" className="logo">
				<img src="/images/favicon.ico" alt="Project Logo" className="logo-image" />
			</a>
			<nav >
				<button onClick={() => (window.location.href = '/home')} className='header button'>Home</button>
				<button onClick={() => navigate('/categories')} className='header button'>Categories</button>
				{isAdmin && <button onClick={() => navigate('/Manage')} className='header button'>Manage</button>}
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
				<button ref={switchButtonRef} onClick={toggleMode}>Switch to {mode === "Dark Mode" ? "Light" : "Dark"}</button>
				<button onClick={handleLogout} className='header button'>Log Out</button>

				{userName && (
                		<div className="user-info">
                			<img src={userImage || "/images/default-avatar.png"} alt="User Avatar" className="user-avatar" />
                			<span>Hello, {userName}</span>
                		</div>
                )}
			</nav>
		</header>
	);
}

export default Header;
