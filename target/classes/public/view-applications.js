/**
 * 
 */



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
				
				if(xhr.status == 201){
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



function addRow(account, table){
	
	
	
	let tableRow = document.createElement("tr");
	let iDCol = document.createElement("td");
	let nameCol = document.createElement("td");
	let descriptionCol = document.createElement("td");
	let approveButton = document.createElement("td");
	
	let rejectButton = document.createElement("td");
	
	tableRow.appendChild(iDCol);
	tableRow.appendChild(nameCol);
	tableRow.appendChild(descriptionCol);
	tableRow.appendChild(approveButton);
	tableRow.appendChild(rejectButton);
	table.appendChild(tableRow);
	
	iDCol.innerHTML = account.id;
	nameCol.innerHTML = account.name;	
	descriptionCol.innerHTML = account.attachedUsername;
	approveButton.innerHTML= '<button type="button" class="green">Approve</button>';
	rejectButton.innerHTML= '<button type="button" class="red">Reject</button>';
	let funky = function(appr){
		console.log("hello");		
		let fd= new FormData();
		fd.append("BankAccount", account.id);
		fd.append("approved", appr);
		let xhr = new XMLHttpRequest();
		xhr.open('POST', "Approve_Account");
		xhr.send( fd);
		tableRow.parentNode.removeChild(tableRow);
	}
	approveButton.addEventListener("click", function(){
		funky('true');
	});
	rejectButton.addEventListener("click", function(){
		funky('false');
	});
};
	
	