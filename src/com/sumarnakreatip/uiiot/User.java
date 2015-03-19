package com.sumarnakreatip.uiiot;

public class User {
	String user_id;
	String npm;
	String nama;
	String asal;
	String tujuan;
	String kapasitas;
	String waktu_berangkat;
	String jam_berangkat;
	String keterangan;

	public String getuser_id() {
		return user_id;
	}

	public void setuser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getnpm() {
		return npm;
	}

	public void setnpm(String npm) {
		this.npm = npm;
	}

	public String getnama() {
		return nama;
	}

	public void setnama(String nama) {
		this.nama = nama;
	}

	public String getasal() {
		return asal;
	}

	public void setasal(String asal) {
		this.asal = asal;
	}

	public String gettujuan() {
		return tujuan;
	}

	public void settujuan(String tujuan) {
		this.tujuan = tujuan;
	}

	public String getkapasitas() {
		return kapasitas;
	}

	public void setkapasitas(String kapasitas) {
		this.kapasitas = kapasitas;
	}

	public String getwaktu_berangkat() {
		return waktu_berangkat;
	}

	public void setwaktu_berangkat(String waktu_berangkat) {
		this.waktu_berangkat = waktu_berangkat;
	}

	public String getjam_berangkat() {
		return jam_berangkat;
	}

	public void setjam_berangkat(String jam_berangkat) {
		this.jam_berangkat = jam_berangkat;
	}
	
	public String getketerangan() {
		return keterangan;
	}

	public void setketerangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public User(String user_id, String npm, String nama, String asal,
			String tujuan, String kapasitas, String waktu_berangkat,
			String jam_berangkat, String keterangan) {
		super();
		this.user_id = user_id;
		this.npm = npm;
		this.nama = nama;
		this.asal = asal;
		this.tujuan = tujuan;
		this.kapasitas = kapasitas;
		this.waktu_berangkat = waktu_berangkat;
		this.jam_berangkat = jam_berangkat;
		this.keterangan = keterangan;
	}

	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", npm=" + npm + ", nama=" + nama
				+ ", asal=" + asal + ", tujuan=" + tujuan + ", kapasitas="
				+ kapasitas + ", waktu_berangkat=" + waktu_berangkat
				+ ", jam_berangkat=" + jam_berangkat + ", keterangan="+keterangan+"]";
	}

}