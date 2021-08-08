/**
 * 
 */
 
 window.onload = function(){
	document.getElementById("reg-button").onclick = function(){
		for(i of document.getElementsByClassName("login")){
			i.innerHTML= "Register";
		}
		document.getElementById("login-form").action= "/register";
		let x= document.getElementById("reg-button");
		x.parentElement.removeChild(x);
	}	
}

