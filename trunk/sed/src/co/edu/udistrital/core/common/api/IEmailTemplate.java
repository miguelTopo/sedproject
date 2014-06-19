package co.edu.udistrital.core.common.api;

public class IEmailTemplate {
	// Nueva cuenta de usuario
	public static final Long NEW_SEDUSER_ACCOUNT = Long.valueOf(1);
	// Recuperar contraseña
	public static final Long PASSWORD_RECOVER = Long.valueOf(2);
	// Nuevo estudiante
	public static final Long NEW_STUDENT = Long.valueOf(3);
	// Modificación de password del usuario
	public static final Long SED_PASSWORD_CHANGE = Long.valueOf(4);
	// Modificación de usuario (número de identificación) del usuario
	public static final Long SED_USER_CHANGE = Long.valueOf(5);
	// Modificación de usuario y contraseña del usuario
	public static final Long SED_PASSWORD_USER_CHANGE = Long.valueOf(6);

}
