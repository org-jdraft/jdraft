package org.jdraft.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public class _urlDownload {
    /** this is the url to the file */
    URL url;

	/*
	public UrlDownload(URL url) {
		this(url), Paths.get(System.getProperty("java.io.tmpdir") ) );
	}
	*/

    public _urlDownload(URL url) {
        this.url = url;
    }

    public File get(String fileName) {
        try {
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            File f = new File(fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel()
                    .transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            return f;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new _ioException("unable to download from "+url+" to "+ fileName);
        }
    }
}
