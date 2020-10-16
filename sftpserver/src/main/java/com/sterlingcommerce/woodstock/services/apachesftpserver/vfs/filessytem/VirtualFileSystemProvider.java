package com.sterlingcommerce.woodstock.services.apachesftpserver.vfs.filessytem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.CopyOption;
import java.nio.file.FileSystem;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.Map;
import java.util.Set;

import org.apache.sshd.common.file.root.RootedFileSystemProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VirtualFileSystemProvider extends RootedFileSystemProvider {

	private static final Logger Log = LoggerFactory.getLogger(VirtualFileSystemProvider.class);

	@Override
	public FileSystem newFileSystem(Path path, Map<String, ?> env) throws IOException {
		// TODO Auto-generated method stub
		return new VirtualFileSystem(this, path, env);
	}
	
	@Override
	public InputStream newInputStream(Path path, OpenOption... options) throws IOException {
		// TODO Auto-generated method stub
		return super.newInputStream(path, options);
	}
	
	@Override
	public OutputStream newOutputStream(Path path, OpenOption... options) throws IOException {
		System.out.println(">> new outputstream :"+path.toString());
		return super.newOutputStream(path, options);
	}
	
	@Override
	public FileChannel newFileChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs)
			throws IOException {
		// TODO Auto-generated method stub
		return super.newFileChannel(path, options, attrs);
	}
	
	@Override
	public void move(Path source, Path target, CopyOption... options) throws IOException {
		// TODO Auto-generated method stub
		super.move(source, target, options);
	}
	
	@Override
	public void copy(Path source, Path target, CopyOption... options) throws IOException {
		// TODO Auto-generated method stub
		super.copy(source, target, options);
	}
	
	@Override
	public void delete(Path path) throws IOException {
		// TODO Auto-generated method stub
		super.delete(path);
	}
}
