function welcome() {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			if (this.responseText) {
				document.getElementById('welcome').innerHTML = 'Hello there, ' +  this.responseText + '!';
				document.getElementById('loginButton').classList.add('hidden');
			} else {
				document.getElementById('loginButton').classList.remove('hidden');
			}
		}
	};
	
	request.open('GET', 'oauth/me', true);
	request.send();
}