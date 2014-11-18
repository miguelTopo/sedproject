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

/** @author MTorres */
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
		PF('statusDialog').show();
		PrimeFaces.ab({
			formId : form,
			partialSubmit : true,
			source : form + ':btnAddStudent',
			process : '@all',
			oncomplete : function(xhr, status, args) {
				PF('statusDialog').hide();
			}
		});
		return false;
	}

}
/** @author MTorres */
 function validateLoadStudentList(){
	var form = "studentListForm";
	
	selectGrade = addSelectValidation(form + ":somStdGrade_input","valStdGradeList_DIV");
	selectCourse = addSelectValidation(form + ":somStdCourse_input","valStdCourseList_DIV");
	
	if (selectGrade && selectCourse) {
		PF('statusDialog').show();
		PrimeFaces.ab({
			formId : form,
			partialSubmit : true,
			source : form + ':btnStudentList',
			process : '@all',
			update: 'studentListForm:opStudentList',
			oncomplete : function(xhr, status, args) {
				PF('statusDialog').hide();
			}
		});
		return false;
	}
 }
 
 function validateAddSedUser(randomPassword, idSedRole){
	 var idForm = "addSedUserForm";
	 
	 valLastName = addTextValidation(idForm + ":txtSuLastName", true, true, 1, 60);
	 valName = addTextValidation(idForm + ":txtSuName", true, true, 1, 60);
	 valIdentification = addTextValidation(idForm + ":txtSuIdentification", true, true, 1, 20);
	 valEmail = addTextValidation(idForm + ":txtSuEmail", true, true, 1, 200);
	 valDateSedUser = addDateValidation(idForm + ":birthdayDate_input", true);
	 
	 selectIdType = addSelectValidation(idForm + ":somIdType_input", "valIdType_DIV");
	 selectRole = addSelectValidation(idForm + ":somSedRole_input", "valIdSedRole_DIV");
	 
	 valid = true;
	 
	 if(!randomPassword){
		 valPass = addTextValidation(idForm + ":userPw", true, true, 6, 50);
		 valPassRetry = addTextValidation(idForm + ":userPwRetry", true, true, 6, 50);
		 valid = LiveValidation.massValidate([ valLastName, valName, valIdentification, valEmail, valDateSedUser, valPass, valPassRetry ]);
	 }else
		 valid = LiveValidation.massValidate([ valLastName, valName, valIdentification, valEmail, valDateSedUser]);
	 
	 //Validacion especifica por usuario
	 var validUserField = true;
	 if(idSedRole == 3){
		 selectGrade = addSelectValidation(idForm + ":somGrade_input", "valGrade_DIV");
		 selectCourse = addSelectValidation(idForm + ":somCourse_input", "valCourse_DIV");
		 validUserField = selectGrade && selectCourse; 
	 }
	 
	 
	 if (valid && selectIdType && selectRole && validUserField) {
			PF('statusDialog').show();
			PrimeFaces.ab({
				formId : idForm,
				partialSubmit : true,
				source : idForm + ':btnAddSedUser',
				process : '@all',
				update :'addSedUserForm:addSedUserPanel',
				oncomplete : function(xhr, status, args) {
					PF('statusDialog').hide();
					PF('sedUserTableWV').clearFilters();
				}
			});
			return false;
		}	  
 }
 /**@author MTorres*/
 function validateUpdatePw(){
	 
	 var idForm = "passwordChangeForm";
	 valOldPass = addTextValidation(idForm + ":txtPasswordOld", true, true, 6, 100);
	 valPass = addTextValidation(idForm + ":txtPassword", true, true, 6, 100);
	 valRetryPass = addTextValidation(idForm + ":txtRetryPassword", true, true, 6, 100);
	 
	 valid = LiveValidation.massValidate([ valOldPass, valPass, valRetryPass ]);
	 
	 if (valid) {
			PF('statusDialog').show();
			PrimeFaces.ab({
				formId : idForm,
				partialSubmit : true,
				source : idForm + ':btnUpdatePw',
				process : '@all',
				update :'passwordChangeForm:passwordChangePanel',
				oncomplete : function(xhr, status, args) {
					PF('statusDialog').hide();
				}
			});
			return false;
		}	
 }
 
 function validateUpdateSedUser(idSedRole){
	 var idForm = "addSedUserForm";
	 
	 valLastName = addTextValidation(idForm + ":txtSuLastName", true, true, 1, 60);
	 valName = addTextValidation(idForm + ":txtSuName", true, true, 1, 60);
	 valIdentification = addTextValidation(idForm + ":txtSuIdentification", true, true, 1, 20);
	 valEmail = addTextValidation(idForm + ":txtSuEmail", true, true, 1, 200);
	 valDateSedUser = addDateValidation(idForm + ":birthdayDate_input", true);
	 
	 valid = LiveValidation.massValidate([valLastName, valName, valIdentification, valEmail, valDateSedUser]);
	 
	 selectIdType = addSelectValidation(idForm + ":somIdType_input", "valIdType_DIV");
	 selectRole = addSelectValidation(idForm + ":somSedRole_input", "valIdSedRole_DIV");
	 
	 var validUserField = true;
	 if(idSedRole == 3){
		 selectGrade = addSelectValidation(idForm + ":somGrade_input", "valGrade_DIV");
		 selectCourse = addSelectValidation(idForm + ":somCourse_input", "valCourse_DIV");
		 validUserField = selectGrade && selectCourse; 
	 }
	 
	 if (valid && selectIdType && selectIdType && validUserField) {
			PF('statusDialog').show();
			PrimeFaces.ab({
				formId : idForm,
				partialSubmit : true,
				source : idForm + ':btnUpdateSedUser',
				process : '@all',
				oncomplete : function(xhr, status, args) {
					PF('statusDialog').hide();
				}
			});
			return false;
		}	
	 
 }
 /**@author MTorres*/
 function validateLoadQualification(){
	 
	 var idForm = "teacherQualificationForm";
	 
	 selectCourse = addSelectValidation(idForm + ":somTeacherCourse_input", "tqCourse_DIV");
	 selectSubject = addSelectValidation(idForm + ":somTeacherSubject_input", "tqSubject_DIV");
	 
	 
	 if (selectCourse && selectSubject) {
			PF('statusDialog').show();
			PrimeFaces.ab({
				formId : idForm,
				partialSubmit : true,
				source : idForm + ':btnLoadQualification',
				process : '@all',
				update :'teacherQualificationForm:studentQualificationOp',
				oncomplete : function(xhr, status, args) {
					PF('statusDialog').hide();
				}
			});
			return false;
		}	
 }
 function validateTracingSearch(){
	 var idForm = "tracingForm";
	 selectPeriod = addSelectValidation(idForm + ":somTracingPeriod_input", "tracingPeriod_DIV");
	 
	 if (selectPeriod) {
			PF('statusDialog').show();
			PrimeFaces.ab({
				formId : idForm,
				partialSubmit : true,
				source : idForm + ':btnTracingSearch',
				process : '@all',
				update :'tracingForm:studentListOp',
				oncomplete : function(xhr, status, args) {
					PF('statusDialog').hide();
				}
			});
			return false;
		}	
 }
 function validateTeacherAssignment(idAssignmentType){
	 
	 
	 var idForm = "selectDateForm";
	 var typeSuccess = true;
	 
	 selectTeacher = addSelectValidation(idForm + ":somSedUser_input", "sedUser_DIV");
	 selectAssignmentType = addSelectValidation(idForm + ":somAssignmentType_input", "teacherAssignmenType_DIV");
	 selectDay = addSelectValidation(idForm + ":somDay_input", "day_DIV");
	 validStartDate = addTextValidation(idForm + ":tpStartDate_input", true, true, 5, 5);
	 validEndDate = addTextValidation(idForm + ":tpEndDate_input", true, true, 5, 5);
	 
	 if(idAssignmentType==1){
		 selectGrade = addSelectValidation(idForm + ":somTeacherGrade_input", "teacherGrade_DIV");
		 selectCourse = addSelectValidation(idForm + ":somTeacherCourse_input", "teacherCourse_DIV");
		 selectSubject = addSelectValidation(idForm + ":somTeacherSubject_input", "teacherSubject_DIV");
		 typeSuccess = selectGrade && selectCourse && selectSubject; 
		 }
		   
	
	 valid = LiveValidation.massValidate([validStartDate, validEndDate]);
	 
	 if (valid && selectTeacher && selectAssignmentType && selectDay && typeSuccess) {
			PF('statusDialog').show();
			PrimeFaces.ab({
				formId : idForm,
				partialSubmit : true,
				source : idForm + ':btnTeacherAssignment',
				process : '@all',
				oncomplete : function(xhr, status, args) {
					PF('statusDialog').hide();
				}
			});
			return false;
		}
 }
 function validateLoadReport(){
	 var idForm = "sedReportForm";
	 selectGrade = addSelectValidation(idForm + ":somReportGrade_input", "reportGrade_DIV");
	 
	 if (selectGrade) {
			PF('statusDialog').show();
			PrimeFaces.ab({
				formId : idForm,
				partialSubmit : true,
				source : idForm + ':btnLoadreport',
				process : '@all',
				update : 'sedReportForm:reportStudentOp',
				oncomplete : function(xhr, status, args) {
					PF('statusDialog').hide();
				}
			});
			return false;
		}
}
 
 function addLoginFormValidation(){
	 var idForm = "loginForm";
	 valUsername = addTextValidation("loginForm:sedUserName", true, true, 1, 100);
	 valPassword = addTextValidation(idForm + ":sedPassword", true, true, 1, 200);
	 return LiveValidation.massValidate([valUsername, valPassword]);
 }
 
function validateLoginForm(){
	var idForm = "loginForm";
	
	if(addLoginFormValidation()){
		
		pass = document.getElementById(idForm + ":sedPassword").value;
		if (pass.length != 32) {
			document.getElementById(idForm + ":sedPassword").value = hex_md5(pass);
		}
		PF('statusDialog').show();
		
		PrimeFaces.ab({
			formId : idForm,
			partialSubmit : true,
			source : idForm + ':btnSedLogin',
			process : idForm,
			update : 'loginForm:growl',
			oncomplete : function(xhr, status, args) {
				loginUserValidate(xhr, status, args);
			}
		});
		return false;
		
	}
	
 }

function validLoginData(){
	var idForm ="loginForm";
	userName = addTextValidation(idForm + ":txtUserName", true, true, 1, 100);
	userPw = addTextValidation(idForm + ":txtUserPassword", true, true, 1, 60);
	return LiveValidation.massValidate([ userName, userPw ]);
}

function loginValidation(){
	if(validLoginData()){
		pass = document.getElementById("loginForm:txtUserPassword").value;
		if (pass.length != 32) {
			document.getElementById("loginForm:txtUserPassword").value = hex_md5(pass);
		}
		PrimeFaces.ab({
			formId : 'loginForm',
			partialSubmit : true,
			source : 'loginForm:btnSedLogin',
			process : 'loginForm',
			update : 'growl',
			oncomplete : function(xhr, status, args) {
				loginUserValidate(xhr, status, args);
			}
		});
		return false;
	}
}

function validateRecoverPw(){
	
	var idForm ="forgetPasswordForm";
	
	valEmail = addEmailValidation(idForm + ":txtSedUserEmail", true, true);

	valid = LiveValidation.massValidate([valEmail]);
	
	 if (valid) {
			PF('statusDialog').show();
			PrimeFaces.ab({
				formId : idForm,
				partialSubmit : true,
				source : idForm + ':btnRecoverPw',
				process : '@all',
				oncomplete : function(xhr, status, args) {
					PF('statusDialog').hide();
				}
			});
			return false;
		}
}

function validateFilterSchedule(scheduleOption, idSedRole){
	var idForm = "calendarAssigmentForm";
	var idButton = "";
	var validOption = true;
	var selectScheduleOption = true;
	
	if(scheduleOption == 1){
		var selectAssignmentType = true;
		idButton = "btnTeacherSearch";
		if(idSedRole != 4 && idSedRole != 3)
			selectAssignmentType = addSelectValidation(idForm + ":filterAssignmentType_input", "filterAssignmentType_DIV");
				
		selectTeacher = addSelectValidation(idForm + ":filterTeacher_input", "filterTeacher_DIV");
		validOption = selectTeacher && selectAssignmentType;
		selectScheduleOption = addSelectValidation(idForm + ":filterOption_input", "filterOption_DIV");
	}else if(scheduleOption == 2){
		idButton = "btnCourseSearch";
		
		selectGrade =  addSelectValidation(idForm + ":filterGrade_input", "filterGrade_DIV");
		selectCourse =  addSelectValidation(idForm + ":filterCourse_input", "filterCourse_DIV");
		selectScheduleOption = addSelectValidation(idForm + ":filterOption_input", "filterOption_DIV");
		validOption = selectGrade && selectCourse; 
	}

	 if (selectScheduleOption && validOption) {
			PF('statusDialog').show();
			PrimeFaces.ab({
				formId : idForm,
				partialSubmit : true,
				source : idForm + ':' + idButton,
				process : '@all',
				oncomplete : function(xhr, status, args) {
					PF('statusDialog').hide();
					PF('assignmentScheduleWV').update();
				}
			});
			return false;
		}
	
	
}