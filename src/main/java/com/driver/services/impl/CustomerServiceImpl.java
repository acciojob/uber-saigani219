package com.driver.services.impl;

import com.driver.model.TripBooking;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Customer;
import com.driver.model.Driver;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;
import com.driver.model.TripStatus;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		customerRepository2.deleteById(customerId);
	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		List<Driver> listOfDrivers = driverRepository2.findAll();
		if(listOfDrivers.size() == 0)
			throw new Exception("No cab available!");
		int driverId = Integer.MAX_VALUE;
//		List<Integer> driverIds = new ArrayList<>();
		for(Driver driverAvailable : listOfDrivers){
			if(driverAvailable.getCab().isAvailable() == false)
				continue;
			driverId = Math.min(driverId, driverAvailable.getDriverId());
		}
		if(driverId == Integer.MAX_VALUE)
		throw new Exception("No cab available!");
		TripBooking tripBooked = new TripBooking(fromLocation, toLocation, distanceInKm, TripStatus.CONFIRMED);

		//Getting driver with id = driverId
		Driver driver = driverRepository2.findById(driverId).get();
		tripBooked.setDriver(driver);
		//Getting customer with id = customerId
		Customer customer = customerRepository2.findById(customerId).get();
		tripBooked.setCustomer(customer);
		//calculating bill
		int bill=distanceInKm*driver.getCab().getPerKmRate();

		tripBooked.setBill(bill);
		driver.getCab().setAvailable(false);

		tripBookingRepository2.save(tripBooked);
		return tripBooked;
	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking currentTrip = tripBookingRepository2.findById(tripId).get();
		currentTrip.setTripStatus(TripStatus.CANCELED);
		currentTrip.setBill(0);

		Driver driver=currentTrip.getDriver();
		driver.getCab().setAvailable(true);
		tripBookingRepository2.save(currentTrip);
	}

	@Override
	public void completeTrip(Integer tripId) {
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking currentTrip = tripBookingRepository2.findById(tripId).get();
		if (currentTrip != null) {
			currentTrip.setTripStatus(TripStatus.COMPLETED);
			Driver driver = currentTrip.getDriver();
			driver.getCab().setAvailable(true);
			tripBookingRepository2.save(currentTrip);
		}
	}
	}
