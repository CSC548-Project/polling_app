import streamlit as st

# In-memory storage for polls
polls = {}

st.sidebar.title("Polling System")
st.sidebar.header("Navigation")
page = st.sidebar.selectbox("Go to", ["Create Poll", "Vote on Poll", "Poll Results"])

if page == "Create Poll":
    st.title("Create a Poll")
    question = st.text_input("Poll Question")
    options = st.text_area("Poll Options (one per line)")
    if st.button("Create Poll"):
        poll_id = len(polls) + 1
        poll_data = {"question": question, "options": options.split("\n")}
        polls[poll_id] = poll_data
        st.write(f"Poll Created with ID: {poll_id}")
        st.write(f"Question: {poll_data['question']}")
        st.write("Options:")
        for option in poll_data['options']:
            st.write(f"- {option}")

elif page == "Vote on Poll":
    st.title("Vote on a Poll")
    poll_id = st.number_input("Poll ID", min_value=1, step=1)
    if poll_id in polls:
        poll_data = polls[poll_id]
        st.write(f"Question: {poll_data['question']}")
        selected_option = st.selectbox("Select an Option", poll_data['options'])
        if st.button("Vote"):
            st.write(f"Vote Submitted for: {selected_option}")
    else:
        st.write("Poll not found. Please enter a valid Poll ID.")

elif page == "Poll Results":
    st.title("Poll Results")
    poll_id_results = st.number_input("Poll ID for Results", min_value=1, step=1)
    if poll_id_results in polls:
        poll_data = polls[poll_id_results]
        st.write(f"Question: {poll_data['question']}")
        st.write("Options:")
        for option in poll_data['options']:
            st.write(f"- {option}")
    else:
        st.write("Poll not found. Please enter a valid Poll ID.")

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
