package org.leplan73.analytiscout.cmd;

import org.leplan73.analytiscout.Progress;

public class CmdProgress implements Progress {
	
	public CmdProgress()
	{
	}

	@Override
	public void setMillisToPopup(int i) {
	}

	@Override
	public void setMillisToDecideToPopup(int i) {
	}

	@Override
	public void setProgress(int i) {
	}

	@Override
	public void setProgress(int i, String note) {
	}

	@Override
	public boolean isCanceled() {
		return false;
	}

	@Override
	public void setNote(String note) {
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}
}
