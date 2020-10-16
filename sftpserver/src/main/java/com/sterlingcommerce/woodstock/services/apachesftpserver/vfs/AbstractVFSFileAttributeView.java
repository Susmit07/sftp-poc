package com.sterlingcommerce.woodstock.services.apachesftpserver.vfs;

import java.nio.file.attribute.FileAttributeView;

public class AbstractVFSFileAttributeView implements FileAttributeView {

	@Override
	public String name() {
		return "view";
	}

}
