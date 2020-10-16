package com.sterlingcommerce.woodstock.services.apachesftpserver.vfs.filessytem;

import java.nio.file.Path;
import java.util.Map;

import org.apache.sshd.common.file.root.RootedFileSystem;

public class VirtualFileSystem extends RootedFileSystem {

	private VirtualFileSystemProvider provider;

	public VirtualFileSystem(VirtualFileSystemProvider fileSystemProvider, Path root, Map<String, ?> env) {
		super(fileSystemProvider, root, env);
		this.provider = fileSystemProvider;
	}

	@Override
	public VirtualFileSystemProvider provider() {
		return provider;
	}
}
