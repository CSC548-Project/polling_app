import streamlit as st

# Initialize session state for poll ID and polls
if 'poll_id' not in st.session_state:
    st.session_state.poll_id = None
if 'polls' not in st.session_state:
    st.session_state.polls = {}

st.sidebar.title("Polling System")
st.sidebar.header("Navigation")
page = st.sidebar.selectbox("Go to", ["Create Poll", "Vote on Poll", "Poll Results"])

if page == "Create Poll":
    st.title("Create a Poll")
    question = st.text_input("Poll Question")
    options = st.text_area("Poll Options (one per line)")
    if st.button("Create Poll"):
        poll_id = len(st.session_state.polls) + 1
        poll_data = {"question": question, "options": options.split("\n"), "votes": [0] * len(options.split("\n"))}
        st.session_state.polls[poll_id] = poll_data
        st.session_state.poll_id = poll_id
        st.write(f"Poll Created with ID: {poll_id}")
        st.write(f"Question: {poll_data['question']}")
        st.write("Options:")
        for option in poll_data['options']:
            st.write(f"- {option}")

elif page == "Vote on Poll":
    st.title("Vote on a Poll")
    poll_id = st.session_state.poll_id if st.session_state.poll_id else st.number_input("Poll ID", min_value=1, step=1)
    if poll_id in st.session_state.polls:
        poll_data = st.session_state.polls[poll_id]
        st.write(f"Question: {poll_data['question']}")
        selected_option = st.selectbox("Select an Option", poll_data['options'])
        if st.button("Vote"):
            option_index = poll_data['options'].index(selected_option)
            poll_data['votes'][option_index] += 1
            st.session_state.polls[poll_id] = poll_data
            st.write(f"Vote Submitted for: {selected_option}")
    else:
        st.write("Poll not found. Please enter a valid Poll ID.")

elif page == "Poll Results":
    st.title("Poll Results")
    poll_id_results = st.number_input("Poll ID for Results", min_value=1, step=1)
    if poll_id_results in st.session_state.polls:
        poll_data = st.session_state.polls[poll_id_results]
        st.write(f"Question: {poll_data['question']}")
        st.write("Options and Votes:")
        for option, votes in zip(poll_data['options'], poll_data['votes']):
            st.write(f"- {option}: {votes} votes")
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
