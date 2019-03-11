import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SecurityUtils {

	public static PrivateKey loadPrivateKey() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
		byte[] keyBytes = Files.readAllBytes(Paths.get("private_key.der"));

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}

	public static PublicKey loadPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] keyBytes = Files.readAllBytes(Paths.get("public_key.der"));

		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}

}
