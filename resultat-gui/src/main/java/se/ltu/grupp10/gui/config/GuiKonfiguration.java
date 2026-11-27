package se.ltu.grupp10.gui.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public record GuiKonfiguration(URI canvasAdress, URI epokAdress) {

    public static GuiKonfiguration lasIn() throws IOException {
        Properties installningar = new Properties();
        InputStream fil = GuiKonfiguration.class.getResourceAsStream("/gui.properties");
        if (fil == null) {
            throw new IOException("Hittar inte gui.properties.");
        }

        try (InputStreamReader lasare = new InputStreamReader(fil, StandardCharsets.UTF_8)) {
            installningar.load(lasare);
        }

        return new GuiKonfiguration(
                lasAdress(installningar, "canvas.url"),
                lasAdress(installningar, "epok.url"));
    }

    private static URI lasAdress(Properties installningar, String nyckel) throws IOException {
        String varde = System.getProperty(nyckel, installningar.getProperty(nyckel));
        if (varde == null || varde.isBlank()) {
            throw new IOException("Serveradress saknas för " + nyckel + ".");
        }

        try {
            URI adress = URI.create(varde.strip());
            if (!("http".equalsIgnoreCase(adress.getScheme())
                    || "https".equalsIgnoreCase(adress.getScheme()))
                    || adress.getHost() == null) {
                throw new IllegalArgumentException("En fullständig HTTP- eller HTTPS-adress krävs.");
            }
            return adress;
        } catch (IllegalArgumentException ex) {
            throw new IOException("Ogiltig serveradress för " + nyckel + ".", ex);
        }
    }
}
