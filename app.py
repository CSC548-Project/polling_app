import streamlit as st

st.sidebar.title("Polling System")
st.sidebar.header("Navigation")
page = st.sidebar.selectbox("Go to", ["Create Poll", "Vote on Poll", "Poll Results"])

if page == "Create Poll":
    st.title("Create a Poll")
    question = st.text_input("Poll Question")
    options = st.text_area("Poll Options (one per line)")
    if st.button("Create Poll"):
        poll_data = {"question": question, "options": options.split("\n")}
        st.write("Poll Created:", poll_data)

elif page == "Vote on Poll":
    st.title("Vote on a Poll")
    poll_id = st.text_input("Poll ID")
    selected_option = st.selectbox("Select an Option", ["Option 1", "Option 2", "Option 3"])
    if st.button("Vote"):
        vote_data = {"option": selected_option}
        st.write("Vote Submitted:", vote_data)

elif page == "Poll Results":
    st.title("Poll Results")
    poll_id_results = st.text_input("Poll ID for Results")
    if st.button("Get Results"):
        results = {"Option 1": 10, "Option 2": 5, "Option 3": 3}
        st.write("Poll Results:", results)

st.markdown(
    """
    <style>
    .stButton>button {
        background-color: #1f77b4;
        color: white;
    }
    </style>
    """,
    unsafe_allow_html=True
)
