package com.concesionario.util;

import javax.swing.*;
import java.awt.*;

// Clase para gestión de notificaciones en la interfaz de usuario
public class NotificationManager {

    // Tipos de notificación
    public enum NotificationType {
        INFO, SUCCESS, WARNING, ERROR
    }

    // Privatizar el constructor para evitar instanciación
    private NotificationManager() {}

    // Muestra una notificación utilizando JOptionPane
    public static void showNotification(Component parent, String message, String title, NotificationType type) {
        int messageType;

        switch (type) {
            case SUCCESS:
            case INFO:
                messageType = JOptionPane.INFORMATION_MESSAGE;
                break;
            case WARNING:
                messageType = JOptionPane.WARNING_MESSAGE;
                break;
            case ERROR:
                messageType = JOptionPane.ERROR_MESSAGE;
                break;
            default:
                messageType = JOptionPane.PLAIN_MESSAGE;
                break;
        }

        JOptionPane.showMessageDialog(parent, message, title, messageType);
    }

    // Muestra una notificación de éxito
    public static void showSuccess(Component parent, String message) {
        showNotification(parent, message, "Éxito", NotificationType.SUCCESS);
    }

    // Muestra una notificación de información
    public static void showInfo(Component parent, String message) {
        showNotification(parent, message, "Información", NotificationType.INFO);
    }

    // Muestra una notificación de advertencia
    public static void showWarning(Component parent, String message) {
        showNotification(parent, message, "Advertencia", NotificationType.WARNING);
    }

    // Muestra una notificación de error
    public static void showError(Component parent, String message) {
        showNotification(parent, message, "Error", NotificationType.ERROR);
    }

    // Muestra una confirmación y devuelve true si el usuario confirma
    public static boolean showConfirmation(Component parent, String message, String title) {
        int result = JOptionPane.showConfirmDialog(
                parent,
                message,
                title,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        return result == JOptionPane.YES_OPTION;
    }

    // Muestra un diálogo de entrada y devuelve el valor introducido
    public static String showInput(Component parent, String message, String title) {
        return JOptionPane.showInputDialog(parent, message, title, JOptionPane.QUESTION_MESSAGE);
    }

    // Muestra un diálogo de selección y devuelve la opción seleccionada
    public static String showSelection(Component parent, String message, String title, Object[] options) {
        Object selectedValue = JOptionPane.showInputDialog(
                parent,
                message,
                title,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        return selectedValue != null ? selectedValue.toString() : null;
    }
}