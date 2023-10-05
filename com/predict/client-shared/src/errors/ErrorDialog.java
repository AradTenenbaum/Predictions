package errors;

public class ErrorDialog {
    public static void send(String error) {
        javafx.scene.control.Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Error");

        dialog.setHeaderText("Message:");
        dialog.setContentText(error);

        dialog.getDialogPane().getButtonTypes().add(javafx.scene.control.ButtonType.OK);
        dialog.setResultConverter(dialogButton -> null);

        dialog.showAndWait();
    }
}
