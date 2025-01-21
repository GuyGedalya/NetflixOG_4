import './SignIn.css';

function SignIn(){
	return(
		<div> 
			<div className="logo-container">
				<img src="/images/favicon.ico" alt="NOG Logo" className="logo"></img>
			</div>
			<div className="text-container">
				<h1>Sign In</h1>
				<form className="login-form">
				<input type="email" id="email" name="email" placeholder="Email or mobile number" required/>
			
				<input type="password" id="password" name="password" placeholder="Password" required/>
				<button type="submit" >Sign In</button>
				</form>
				<p>New to NOG? <a href="/signup.html">Sign up now.</a></p>
			</div>
	  </div>
	);
}

export default SignIn;
