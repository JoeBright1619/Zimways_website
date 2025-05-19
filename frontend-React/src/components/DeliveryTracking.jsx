function DeliveryTracking() {
    const driver = {
      name: 'John Doe',
      phone: '123-456-7890',
      status: 'On the way',
    };
  
    return (
      <div className="delivery-tracking">
        <h2>Delivery Tracking</h2>
        <p>Driver: {driver.name}</p>
        <p>Phone: {driver.phone}</p>
        <p>Status: {driver.status}</p>
      </div>
    );
  }
  
  export default DeliveryTracking;