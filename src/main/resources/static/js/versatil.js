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

function selecionarAfiliado(idAfiliado) {
	$.ajax({
		url: "/afiliado/selecionar/" + idAfiliado,
		success: function() {
			location.reload();
		}
	}).fail(function(response) {
		var error = JSON.parse(response.responseText);
		console.log(error);
	});
}

function getMes(mes) {
		if (mes == 1) {
			return "Janeiro";
		} else if (mes == 2) {
			return "Fevereiro";
		} else if (mes == 3) {
			return "Março";
		} else if (mes == 4) {
			return "Abril";
		} else if (mes == 5) {
			return "Maio";
		} else if (mes == 6) {
			return "Junho";
		} else if (mes == 7) {
			return "Julho";
		} else if (mes == 8) {
			return "Agosto";
		} else if (mes == 9) {
			return "Setembro";
		} else if (mes == 10) {
			return "Outubro";
		} else if (mes == 11) {
			return "Novembro";
		} else if (mes == 12) {
			return "Dezembro";
		}
}
