package com.sterlingcommerce.woodstock.services.apachesftpserver.vfs;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.sshd.common.file.FileSystemFactory;
import org.apache.sshd.common.session.SessionContext;
import org.apache.sshd.common.util.ValidateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sterlingcommerce.woodstock.services.apachesftpserver.vfs.filessytem.VirtualFileSystemProvider;

public class ExchangeFileSystemFactory implements FileSystemFactory {

	private static final String CLASS_NAME = "ExchangeFileSystemFactory.";
	protected static final Logger log = LoggerFactory.getLogger(CLASS_NAME);

	private Path defaultHomeDir;
	private final Map<String, Path> homeDirs = new ConcurrentHashMap<>();

	public ExchangeFileSystemFactory() {
		super();
	}

	public ExchangeFileSystemFactory(Path defaultHomeDir) {
		this.defaultHomeDir = defaultHomeDir;
	}

	public void setDefaultHomeDir(Path defaultHomeDir) {
		this.defaultHomeDir = defaultHomeDir;
	}

	public Path getDefaultHomeDir() {
		return defaultHomeDir;
	}

	public void setUserHomeDir(String userName, Path userHomeDir) {
		homeDirs.put(ValidateUtils.checkNotNullAndNotEmpty(userName, "No username"),
				Objects.requireNonNull(userHomeDir, "No home dir"));
	}

	public Path getUserHomeDir(String userName) {
		return homeDirs.get(ValidateUtils.checkNotNullAndNotEmpty(userName, "No username"));
	}

	@Override
	public Path getUserHomeDir(SessionContext session) throws IOException {
		String username = session.getUsername();
		Path homeDir = getUserHomeDir(username);
		if (homeDir == null) {
			homeDir = getDefaultHomeDir();
		}

		return homeDir;
	}

	@Override
	public FileSystem createFileSystem(SessionContext session) throws IOException {
		Path dir = getUserHomeDir(session);
		if (dir == null) {
			throw new InvalidPathException(session.getUsername(), "Cannot resolve home directory");
		}

		return new VirtualFileSystemProvider().newFileSystem(dir, Collections.emptyMap());
	}

}