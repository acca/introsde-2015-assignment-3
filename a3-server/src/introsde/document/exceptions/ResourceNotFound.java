package introsde.document.exceptions;

public class ResourceNotFound extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFound() {
		super("Resource not found on DB");
	}
}
