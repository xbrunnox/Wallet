function formatarNumero(numero) {
	if (!numero)
		return '-';
	return numero.toLocaleString('pt-BR'); // "9.876.543,21"
}

function formatarDecimal(numero) {
	if (!numero)
		return '-';
	return numero.toLocaleString('pt-BR', {
		minimumFractionDigits: 2,
		maximumFractionDigits: 2
	}); // "9.876.543,21"
}

function formatarDecimal(numero, casasDecimais) {
	if (!numero)
		return '-';
	return numero.toLocaleString('pt-BR', {
		minimumFractionDigits: casasDecimais,
		maximumFractionDigits: casasDecimais
	}); // "9.876.543,21"
}

function formatarDateToDdMmYyyy(data) {
	const yyyy = data.getFullYear();
	let mm = data.getMonth() + 1; // Months start at 0!
	let dd = data.getDate();

	if (dd < 10)
		dd = '0' + dd;
	if (mm < 10)
		mm = '0' + mm;

	return dd + '/' + mm + '/' + yyyy;
}

function extractHourFromOffsetDate(offsetDate) {
	// 2023-05-03T09:01:55
	return offsetDate.substring(11, 13);
}

function extractMinutesFromOffsetDate(offsetDate) {
	// 2023-05-03T09:01:55
	return offsetDate.substring(14, 16);
}

function extractHourMinutesFromOffsetDate(offsetDate) {
	// 2023-05-03T09:01:55
	return offsetDate.substring(11, 16);
}