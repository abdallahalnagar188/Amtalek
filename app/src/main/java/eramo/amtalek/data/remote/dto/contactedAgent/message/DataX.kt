package eramo.amtalek.data.remote.dto.contactedAgent.message


import com.google.gson.annotations.SerializedName

data class DataX(
    @SerializedName("agent_data")
    var agentData: AgentData?
)