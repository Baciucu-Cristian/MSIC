package control.SiteSettings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Properties;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

public class AdminPassword
{
    public String parola;
    public Properties adminProperties;
    public String error;
    public String webSiteLocation;
    String localPath;
    SecretKeySpec key;
    Cipher cipher;
    
    public AdminPassword(String webSiteLocation)
    {
        this.webSiteLocation = webSiteLocation;
        localPath = "\\Config\\admin.txt";
        adminProperties = new Properties();
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
            0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
        key = new SecretKeySpec(keyBytes, "AES");
        try
        {
            cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        }
        catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException ex)
        {
            error = "error";
        }
    }
    
    public void init()
    {
        parola = "administrator";
        if(!readData())
        {
            writeData();
        }
    }
    
    public void writeData()
    {
        error = "no error";
        try
        {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherText = new byte[cipher.getOutputSize(parola.getBytes().length)];
            int ctLength = cipher.update(parola.getBytes(), 0, parola.getBytes().length, cipherText, 0);
            ctLength += cipher.doFinal(cipherText, ctLength);
            adminProperties.setProperty("parola", new String(cipherText, StandardCharsets.ISO_8859_1));

            try
            {
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(webSiteLocation + localPath), "ISO_8859_1"));

                adminProperties.store(out, "Admin Data");
                out.close();
            }
            catch (IOException e)
            {
                error = "error";
            }

            File source = new File(webSiteLocation + localPath);

            Path path = Paths.get(webSiteLocation);
            path = path.getParent().getParent();

            String tempPath = path.toAbsolutePath().toString() + "\\web" + localPath;
            File target = new File(tempPath);

            try
            {
                Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException ex)
            {
            }
        }
        catch (InvalidKeyException | ShortBufferException | IllegalBlockSizeException | BadPaddingException e)
        {
            error = "error";
        }
    }
    
    void setData() throws InvalidKeyException, ShortBufferException, IllegalBlockSizeException, BadPaddingException
    {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainText = new byte[cipher.getOutputSize(adminProperties.getProperty("parola").getBytes(StandardCharsets.ISO_8859_1).length)];
        int ptLength = cipher.update(adminProperties.getProperty("parola").getBytes(StandardCharsets.ISO_8859_1), 0, adminProperties.getProperty("parola").getBytes(StandardCharsets.ISO_8859_1).length, plainText, 0);
        ptLength += cipher.doFinal(plainText, ptLength);
        
        parola = new String(plainText);
        parola = parola.substring(0, parola.indexOf(0));
    }
    
    public boolean readData()
    {
        error = "no error";
        try
        {
            Reader in = new BufferedReader(new InputStreamReader(new FileInputStream(webSiteLocation + localPath), "ISO_8859_1"));
            adminProperties.load(in);
            setData();
            in.close();
        }
        catch(IOException | InvalidKeyException | ShortBufferException | IllegalBlockSizeException | BadPaddingException e)
        {
            error = "error";
            return false;
        }
         
        return true;
    }
}
