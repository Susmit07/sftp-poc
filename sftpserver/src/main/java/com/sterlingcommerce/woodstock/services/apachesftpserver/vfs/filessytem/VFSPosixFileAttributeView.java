package com.sterlingcommerce.woodstock.services.apachesftpserver.vfs.filessytem;

import java.io.IOException;
import java.nio.file.attribute.FileTime;
import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.Set;

import com.sterlingcommerce.woodstock.services.apachesftpserver.vfs.AbstractVFSFileAttributeView;

public class VFSPosixFileAttributeView extends AbstractVFSFileAttributeView implements PosixFileAttributeView {

    
	@Override
	public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("sdas");

	}

	@Override
	public UserPrincipal getOwner() throws IOException {
		// TODO Auto-generated method stub
		System.out.println("sdas");
		return null;
	}

	@Override
	public void setOwner(UserPrincipal owner) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "posix";
	}

	@Override
	public PosixFileAttributes readAttributes() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPermissions(Set<PosixFilePermission> perms) throws IOException {
		// TODO Auto-generated method stub
		

	}

	@Override
	public void setGroup(GroupPrincipal group) throws IOException {
		// TODO Auto-generated method stub

	}

}
