package com.sportcoachhelper.util;

import android.graphics.Paint;

import com.sportcoachhelper.model.Team;

public class TeamManager {
	
	private Team teamA;
	private Team teamB;

	private TeamManager(){
		teamA = new Team(1);
		teamB = new Team(2);
		
		Paint playerTeamAPaint = new Paint();
		playerTeamAPaint.setAntiAlias(true);
		playerTeamAPaint.setDither(true);
		playerTeamAPaint.setColor(0xFF000000);
		playerTeamAPaint.setStyle(Paint.Style.FILL);
		playerTeamAPaint.setStrokeJoin(Paint.Join.ROUND);
		playerTeamAPaint.setStrokeCap(Paint.Cap.ROUND);
		playerTeamAPaint.setStrokeWidth(4);
		
		Paint playerTeamBPaint = new Paint();
		playerTeamBPaint.setAntiAlias(true);
		playerTeamBPaint.setDither(true);
		playerTeamBPaint.setColor(0xFFFF0000);
		playerTeamBPaint.setStyle(Paint.Style.FILL);
		playerTeamBPaint.setStrokeJoin(Paint.Join.ROUND);
		playerTeamBPaint.setStrokeCap(Paint.Cap.ROUND);
		playerTeamBPaint.setStrokeWidth(4);
		
		teamA.setPaint(playerTeamAPaint);
		teamB.setPaint(playerTeamBPaint);
		
		
	}
	
	public Team getTeamA() {
		return teamA;
	}

	public void setTeamA(Team teamA) {
		this.teamA = teamA;
	}

	public Team getTeamB() {
		return teamB;
	}

	public void setTeamB(Team teamB) {
		this.teamB = teamB;
	}

	private static TeamManager instance = new TeamManager();
	
	public static TeamManager getInstance(){
		return instance;
	}

	public Paint getPaintForTeam(int team) {
		
		if(teamA.getNumber()==team) {
			return teamA.getPaint();
		} else if (teamB.getNumber()==team) {
			return teamB.getPaint();
		}
		return null;
		
	}

	public Team getTeam(int team) {
		Team result = null;
		
		if(teamA.getNumber()==team) {
			result = teamA;
		} else if(teamB.getNumber()==team) {
			result = teamB;
		}
		return result;
	}
	
}
