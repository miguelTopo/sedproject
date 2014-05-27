function parseLowerCase(form, idElement) {
	var input = document.getElementById(form + ":" + idElement);
	input.value = input.value.toLowerCase();

}

function addElement(divName, text, addAstk) {
	var ni = document.getElementById(divName);
	if (ni != null) {
		var newdiv = document.createElement(divName + 'LV');
		newdiv.setAttribute('id', divName + 'LV');
		newdiv.innerHTML = (addAstk ? '* ' : '') + text;
		ni.appendChild(newdiv);
	}
}

function removeElement(divName) {
	var d = document.getElementById(divName);
	var olddiv = document.getElementById(divName + 'LV');
	if (olddiv != null)
		d.removeChild(olddiv);
}

function isDate(dat) {
	var t = new Date(dat);
	return isNaN(t.valueOf());
}

function addSelectValidation(componentId, divId){
	removeElement(divId);
	if (document.getElementById(componentId) != null && document.getElementById(componentId).selectedIndex == 0) {
		addElement(divId, lmMustSelectOne, true);
		return false;
	}
	return true;
}

function addDateValidation(componentId, compulsory){
	var lv = new LiveValidation(componentId, {
		onlyOnSubmit : compulsory,
		validMessage : " ",
		wait : 500
	});
	if (isDate(document.getElementById(componentId).value))
		lv.add(Validate.Format, {
			pattern : /live/i
		});
	lv.add(Validate.Presence);
	return lv;
}

function addEmailValidation(idComponent,compulsory, presence){
	var lv = new LiveValidation(idComponent, {
		onlyOnSubmit : compulsory,
		validMessage : " ",
		wait : 500
	});
	lv.add(Validate.Email);
	lv.add(Validate.Exclusion, {
		within : [ "!", "\xb7", "#", "$", "%", "&", "/", "(", ")", "=", "?", "\xbf", "*", "+", "\xe7", ",", ";", ":", "<", ">", "\xba", "|" ],
		allowNull : false,
		partialMatch : true,
		caseSensitive : false,
		failureMessage : lmIC
	});
	if (presence) {
		lv.add(Validate.Presence);
	}
	return lv;
}

function addTextValidation(idComponent, compulsory, presence, minLength,
		maxLength) {
	var lv = new LiveValidation(idComponent, {
		onlyOnSubmit : compulsory,
		validMessage : " ",
		wait : 500
	});
	lv.add(Validate.Exclusion, {
		within : [ "--" ],
		allowNull : false,
		partialMatch : true,
		caseSensitive : false,
		failureMessage : lmIC
	});
	lv.add(Validate.Length, {
		minimum : minLength,
		maximum : maxLength
	});
	if (presence) {
		lv.add(Validate.Presence);
	}
	return lv;
}

function validateAddStudent() {
	var form = "addStudentForm";
	txtName = addTextValidation(form + ":txtStdName", true, true, 1, 20);
	txtLastName = addTextValidation(form + ":txtStdLastname", true, true, 1,20);
	txtEmail = addEmailValidation(form + ":txtStdEmail", true, true);
	txtIdentification = addTextValidation(form + ":txtDocument", true, true, 1, 20);
	stdBirthday = addDateValidation(form + ":cStdBirthday_input", true);
	
	selectIdType = addSelectValidation(form + ":somIdentificationType_input", "valIdType_DIV");
	selectGrade = addSelectValidation(form + ":somGrade_input", "valGrade_DIV");
	selectCourse = addSelectValidation(form + ":somCourse_input", "valCourse_DIV");
	
	valid = LiveValidation.massValidate([ txtName, txtLastName, txtEmail, stdBirthday, txtIdentification ]);
	if (valid && selectIdType && selectGrade && selectCourse) {
		statusDialog.show();
		Primefaces.ab({
			formId : form,
			partialSubmit : true,
			source : form + ':btnAddStudent',
			process : '@all',
			update : '',
			oncomplete : function(xhr, status, args) {
				statusDialog.hide();
			}
		});
		return false;
	}

}