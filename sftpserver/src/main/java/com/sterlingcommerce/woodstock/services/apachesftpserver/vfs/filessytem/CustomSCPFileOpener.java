package com.sterlingcommerce.woodstock.services.apachesftpserver.vfs.filessytem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

import org.apache.sshd.common.scp.ScpFileOpener;
import org.apache.sshd.common.scp.ScpSourceStreamResolver;
import org.apache.sshd.common.scp.ScpTargetStreamResolver;
import org.apache.sshd.common.scp.helpers.LocalFileScpSourceStreamResolver;
import org.apache.sshd.common.scp.helpers.LocalFileScpTargetStreamResolver;
import org.apache.sshd.common.session.Session;

public class CustomSCPFileOpener implements ScpFileOpener {

	@Override
	public InputStream openRead(Session session, Path file, long size, Set<PosixFilePermission> permissions,
			OpenOption... options) throws IOException {
		if (isUserAuthorized(session, "READ")) {
			return Files.newInputStream(file, options);
		} else {
			return null;
		}
	}

	@Override
	public ScpSourceStreamResolver createScpSourceStreamResolver(Session session, Path path) throws IOException {
		return new LocalFileScpSourceStreamResolver(path, this);
	}

	@Override
	public OutputStream openWrite(Session session, Path file, long size, Set<PosixFilePermission> permissions,
			OpenOption... options) throws IOException {
		if (isUserAuthorized(session, "WRITE")) {
			return Files.newOutputStream(file, options);
		} else {
			return null;
		}
	}

	@Override
	public ScpTargetStreamResolver createScpTargetStreamResolver(Session session, Path path) throws IOException {
		return new LocalFileScpTargetStreamResolver(path, this);
	}

	private boolean isUserAuthorized(Session session, String opsType) {
		if (session.getUsername().equalsIgnoreCase("admin")) {
			System.out.println("User is valid to perform the " + opsType + " operation for SCP protocol");
			return true;
		} else {
			return false;
		}

	}

}
