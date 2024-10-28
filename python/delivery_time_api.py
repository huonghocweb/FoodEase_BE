from flask import Flask, request, jsonify
import joblib
import numpy as np

# Tải mô hình
model = joblib.load('delivery_time_model.pkl')

app = Flask(__name__)

@app.route('/predict', methods=['POST'])
def predict():
    data = request.get_json()
    features = np.array([[data['distance'], data['weather'], data['order_time'], data['order_volume']]])
    prediction = model.predict(features)
    return jsonify({'estimated_delivery_time': prediction[0]})

if __name__ == '__main__':
    app.run(port=5000, debug=True)
