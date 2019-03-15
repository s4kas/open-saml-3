import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class SecurityUtils {

	public static PrivateKey loadPrivateKey() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
		byte[] keyBytes = Files.readAllBytes(Paths.get("private_key.der"));

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}

	public static X509Certificate loadCertificate() throws CertificateException, FileNotFoundException {
		CertificateFactory fact = CertificateFactory.getInstance("X.509");
		FileInputStream is = new FileInputStream ("certificate.pem");
		return (X509Certificate) fact.generateCertificate(is);
	}

}
