package simuframe;

/**
 * @author m-morita
 * This exception class is thrown when SimuSetting is invalid.
 */
public class InvalidSimuSettingException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public InvalidSimuSettingException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

	public InvalidSimuSettingException(String arg0) {
		super(arg0);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

	public InvalidSimuSettingException(Throwable arg0) {
		super(arg0);
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

	public InvalidSimuSettingException() {
		super();
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}

}
