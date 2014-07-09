package co.edu.udistrital.core.common.api;

public class IEmailTemplate {
	// Nueva cuenta de usuario
	public static final Long NEW_SEDUSER_ACCOUNT = Long.valueOf(1);
	// Recuperar contrase�a
	public static final Long PASSWORD_RECOVER = Long.valueOf(2);
	// Nuevo estudiante
	public static final Long NEW_STUDENT = Long.valueOf(3);
	// Modificaci�n de password del usuario
	public static final Long SED_PASSWORD_CHANGE = Long.valueOf(4);
	// Modificaci�n de usuario (n�mero de identificaci�n) del usuario
	public static final Long SED_USER_CHANGE = Long.valueOf(5);
	// Modificaci�n de usuario y contrase�a del usuario
	public static final Long SED_PASSWORD_USER_CHANGE = Long.valueOf(6);
	//Modificación de password usuario 
	public static final Long PASSWORD_UPDATE= Long.valueOf(7);

}
