package com.unos.crescentapp.intefaces;

import com.unos.crescentapp.models.FindingPeople;

public interface OnCardDismissedListener {
	void onLike(FindingPeople man);
    void onDislike(FindingPeople man);
}
