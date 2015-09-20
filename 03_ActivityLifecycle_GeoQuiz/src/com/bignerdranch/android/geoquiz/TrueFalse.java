package com.bignerdranch.android.geoquiz;

public class TrueFalse {
    private int mQuestion;
    // this variable will hold a resource ID for a string

    private boolean mTrueQuestion;
    private boolean mIsCheatear;
    public TrueFalse(int question, boolean trueQuestion) {
        mQuestion = question;
        mTrueQuestion = trueQuestion;
    }

	

	public boolean isIsCheatear() {
		return mIsCheatear;
	}



	public void setIsCheatear(boolean isCheatear) {
		mIsCheatear = isCheatear;
	}



	public int getQuestion() {
		return mQuestion;
	}

	public void setQuestion(int question) {
		mQuestion = question;
	}

	public boolean isTrueQuestion() {
		return mTrueQuestion;
	}

	public void setTrueQuestion(boolean trueQuestion) {
		mTrueQuestion = trueQuestion;
	}

}
