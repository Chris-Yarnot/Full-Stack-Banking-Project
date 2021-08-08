
window.onload = function(){
	grabAccounts("View_transactions", document.getElementById("table"), )
	
	
	
}



function grabAccounts(address, table){
	table.innerHTML= "";
	let xhr = new XMLHttpRequest();
	
	const url = "/"+ address;
	
	xhr.onreadystatechange = function(){
		
		console.log("hi!");
		console.log(xhr.readyState);
		switch(xhr.readyState){
			
			case 0:
				console.log("nothing, not initalized yet!");
				break;
			case 1: 
				console.log("connection established");
				break;
			case 2:
				console.log("request sent");
				break;
			case 3:
				console.log("awaiting request");
				break;
			case 4: 
				console.log(xhr.status)
				
				if(xhr.status == 200){
					console.log(xhr.responseText);
					
					let accountList = JSON.parse(xhr.responseText);
					
					console.log(accountList);
					
					accountList.forEach(
						element => {
							addRow(element, table);
						}
						
					)
					

				}
		}
		
		
		
	}
	xhr.open("GET",url);
	
	xhr.send();
}



function addRow(account, table, updateId){
	
	
	
	let tableRow = document.createElement("tr");
	let iDCol = document.createElement("td");
	let nameCol = document.createElement("td");
	let descriptionCol = document.createElement("td");
	
	
	tableRow.appendChild(iDCol);
	tableRow.appendChild(nameCol);
	tableRow.appendChild(descriptionCol);
	
	table.appendChild(tableRow);
	
	iDCol.innerHTML = account.bankAccountTo;
	nameCol.innerHTML = account.bankAccountFrom;	
	descriptionCol.innerHTML = account.ammount;
};
	
	