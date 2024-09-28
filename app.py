import streamlit as st

st.title("Polling System")
st.header("Create a Poll")

st.header("Create a Poll")
question = st.text_input("Poll Question")
options = st.text_area("Poll Options (one per line)")

if st.button("Create Poll"):
    poll_data = {"question": question, "options": options.split("\n")}
    st.write("Poll Created:", poll_data)

st.header("Vote on a Poll")
poll_id = st.text_input("Poll ID")
selected_option = st.selectbox("Select an Option", ["Option 1", "Option 2", "Option 3"])

if st.button("Vote"):
    vote_data = {"option": selected_option}
    st.write("Vote Submitted:", vote_data)

st.header("Poll Results")
poll_id_results = st.text_input("Poll ID for Results")

if st.button("Get Results"):
    # Mock results for demonstration
    results = {"Option 1": 10, "Option 2": 5, "Option 3": 3}
    st.write("Poll Results:", results)
