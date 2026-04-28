// entity/Passenger.java
package com.rideon.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "passengers")
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int passengerId;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "passenger_name", nullable = false)
    private String passengerName;
    
    @Column(name = "age" , nullable = false)
    private int age;
    
    @Column(name = "gender" , nullable = false)
    private String gender;
    
    @Column(name = "seat_no")
    private String seatNo;

    public Passenger() {}

    public Passenger(Booking booking, String passengerName, int age, String gender, String seatNo) {
        this.booking = booking;
        this.passengerName = passengerName;
        this.age = age;
        this.gender = gender;
        this.seatNo = seatNo;
    }

	public int getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(int passengerId) {
		this.passengerId = passengerId;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public String getPassengerName() {
		return passengerName;
	}

	public void setPassengerName(String passengerName) {
		this.passengerName = passengerName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSeatNo() {
		return seatNo;
	}

	public void setSeatNo(String seatNo) {
		this.seatNo = seatNo;
	}
    
    
}