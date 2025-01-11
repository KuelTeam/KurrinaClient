package cn.kuelcancel.kurrina.management.quickplay;

import java.util.ArrayList;

import cn.kuelcancel.kurrina.management.quickplay.impl.ArcadeQuickPlay;
import cn.kuelcancel.kurrina.management.quickplay.impl.BedwarsQuickPlay;
import cn.kuelcancel.kurrina.management.quickplay.impl.DuelsQuickPlay;
import cn.kuelcancel.kurrina.management.quickplay.impl.MainLobbyQuickPlay;
import cn.kuelcancel.kurrina.management.quickplay.impl.MurderMysteryQuickPlay;
import cn.kuelcancel.kurrina.management.quickplay.impl.SkywarsQuickPlay;
import cn.kuelcancel.kurrina.management.quickplay.impl.TNTQuickPlay;
import cn.kuelcancel.kurrina.management.quickplay.impl.UHCQuickPlay;

public class QuickPlayManager {

	private ArrayList<QuickPlay> quickPlays = new ArrayList<QuickPlay>();
	
	public QuickPlayManager() {
		quickPlays.add(new ArcadeQuickPlay());
		quickPlays.add(new BedwarsQuickPlay());
		quickPlays.add(new DuelsQuickPlay());
		quickPlays.add(new MainLobbyQuickPlay());
		quickPlays.add(new MurderMysteryQuickPlay());
		quickPlays.add(new SkywarsQuickPlay());
		quickPlays.add(new TNTQuickPlay());
		quickPlays.add(new UHCQuickPlay());
	}

	public ArrayList<QuickPlay> getQuickPlays() {
		return quickPlays;
	}
}
