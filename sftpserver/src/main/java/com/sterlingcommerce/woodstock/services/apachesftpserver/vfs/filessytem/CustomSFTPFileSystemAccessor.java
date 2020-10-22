package com.sterlingcommerce.woodstock.services.apachesftpserver.vfs.filessytem;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

import org.apache.sshd.common.util.GenericUtils;
import org.apache.sshd.common.util.io.IoUtils;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.subsystem.sftp.SftpFileSystemAccessor;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemProxy;

public class CustomSFTPFileSystemAccessor implements SftpFileSystemAccessor {

	@Override
	public void renameFile(ServerSession session, SftpSubsystemProxy subsystem, Path oldPath, Path newPath,
			Collection<CopyOption> opts) throws IOException {
		if (isUserAuthorized(session, "RENAME")) {
			Files.move(oldPath, newPath, GenericUtils.isEmpty(opts) ? IoUtils.EMPTY_COPY_OPTIONS
					: opts.toArray(new CopyOption[opts.size()]));
		}
	}

	@Override
	public void createDirectory(ServerSession session, SftpSubsystemProxy subsystem, Path path) throws IOException {
		if (isUserAuthorized(session, "CREATE")) {
			Files.createDirectory(path);
		}
	}

	@Override
	public void removeFile(ServerSession session, SftpSubsystemProxy subsystem, Path path, boolean isDirectory)
			throws IOException {
		if (isUserAuthorized(session, "DELETE")) {
			Files.delete(path);
		}
	}

	private boolean isUserAuthorized(ServerSession session, String opsType) {
		if (session.getUsername().equalsIgnoreCase("admin")) {
			System.out.println("User is valid to perform the " + opsType + " operation for SFTP protocol");
			return true;
		} else {
			return false;
		}

	}

}
