function parseLowerCase(form, idElement) {
	var input = document.getElementById(form + ":" + idElement);
	input.value = input.value.toLowerCase();  
	
}