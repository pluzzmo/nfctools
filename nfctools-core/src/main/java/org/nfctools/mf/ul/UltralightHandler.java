package org.nfctools.mf.ul;

import org.nfctools.mf.block.MfBlock;

public class UltralightHandler {

	public static boolean isBlank(MfBlock[] blocks) {
		if (blocks.length < 5)
			throw new IllegalArgumentException("need at least 5 blocks");
		if (blocks[0].getData()[0] == (byte)0x04) // manufacturer ID
		{
			if (isStaticallyUnlocked(blocks[2].getData()) && isOTPClear(blocks[3].getData())) {
				return isUltralight(blocks[4].getData()) || isUltralightC(blocks[4].getData());
			}
		}
		return false;
	}

	private static boolean isOTPClear(byte[] data) {
		for (int x = 0; x < 4; x++) {
			if (data[x] != 0)
				return false;
		}
		return true;
	}

	public static boolean isStaticallyUnlocked(byte[] block) {
		return block[2] == 0 && block[3] == 0;
	}

	public static boolean isUltralight(byte[] block) {
		return matchesVersion(block, 0xff, 0xff);
	}

	public static boolean isUltralightC(byte[] block) {
		return matchesVersion(block, 0x02, 0x00);
	}

	private static boolean matchesVersion(byte[] block, int major, int minor) {
		return block[0] == (byte)major && block[1] == (byte)minor;
	}

	public static boolean isFormatted(MfBlock[] blocks) {
		return isCapabilityContainerValid(blocks[3].getData());
	}

	private static boolean isCapabilityContainerValid(byte[] block) {
		return block[0] == (byte)0xE1 && block[1] == (byte)0x10 && block[2] >= (byte)0x06;
	}
}